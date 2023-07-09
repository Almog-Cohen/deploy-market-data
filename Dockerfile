FROM node:16.17.0-bullseye-slim

WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Copy the rest of the application code to the working directory
# and change the ownership to the 'node' user
COPY --chown=node:node . .

# Securtiy - Reduce the user permissions
#USER node

RUN npm install --legacy-peer-deps


# Build the Next.js app
RUN npm run build

# Passing Environment Arguments
# ENV PORT $PORT
# ENV PROJ_NAME $PROJ_NAME
EXPOSE $PORT

CMD [ "npm", "run", "prod" ]
