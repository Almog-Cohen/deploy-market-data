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

# Passing Build Arguments
ARG PORT
ARG MONGDODB_CONNECTION_STRING

# Passing Environment Variables
ENV NODE_CACHE_TIMER $NODE_CACHE_TIMER
ENV IS_DEV $IS_DEV
ENV BUILD_VERSION $BUILD_VERSION

# Container Exposed Port
EXPOSE $PORT

CMD [ "npm", "run", "prod" ]
