pipeline {
    agent any
    environment{
    EMAIL = "dorsinai1004@gmail.com"
    DOCKERHUB_CREDENTIALS = credentials('docker-hub')
    CONTAINER_NAME = "stock-app"
    IMAGE_NAME = "$DOCKERHUB_CREDENTIALS_USR/$CONTAINER_NAME"
    PORT = "3000"
    }
    stages {
        stage("Clone Git Repository") {
            steps {
                git(
                    url: "git@github.com:Almog-Cohen/Market-data.git",
                    branch: "master",
                    credentialsId: "ssh",
                    changelog: true,
                    poll: true
                )
            }
        }
        stage('Build docker image'){
            steps{
                sh "docker build -t $IMAGE_NAME ."
                }
        }
        stage('Push to Docker Hub'){
            steps{
                sh "echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin"
                sh "docker push $IMAGE_NAME"
                }
        }
        stage('Deploy on VPS'){
            steps{
                sh "ansible-playbook -i hosts playbook.yaml"
                }
            }
    }
    post{
        always{
            sh "docker logout"
            sh "docker rmi -f $IMAGE_NAME"
        }
        success{    mail(body: "The Network Backup ${env.BUILD_URL} has been executed successfully",
                     subject: "Succeeded Pipeline: ${currentBuild.fullDisplayName}",
                     to: "$EMAIL")
        }
        failure{
                mail(body: "Something is wrong with ${env.BUILD_URL}",
                     subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                     to: "$EMAIL")
        }
    }
}