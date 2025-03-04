##############
# BASE STAGE #
##############

# Install the dependencies (creates the node_modules dir).

FROM node:18-alpine AS base

WORKDIR /app

EXPOSE 3000

RUN apk add --no-cache curl git openssl 

ENV PATH="/app/node_modules/.bin:$PATH" 

# Define common entrypoint and healtcheck

COPY ./entrypoint.sh /entrypoint.sh
RUN chmod 550 /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s \
    CMD curl -f http://127.0.0.1:3000/ || exit 1 

###############
# BUILD STAGE #
###############

FROM base AS build

WORKDIR /app

COPY package*.json ./

COPY public/ /app/public/
COPY src/ /app/src/
COPY tsconfig.json /app/
COPY next.config.ts /app/

RUN npm ci --omit=dev && npm cache clean --force

RUN npm run build

####################
# PRODUCTION STAGE #
####################

FROM base AS prod

ENV NEXT_PUBLIC_ENVIRONMENT=prod
ENV NEXT_TELEMETRY_DISABLED=1

COPY --from=build /app/package.json /app/
COPY --from=build /app/package-lock.json /app/
COPY --from=build /app/node_modules /app/node_modules
COPY --from=build /app/.next /app/.next
COPY --from=build /app/public ./public

CMD ["start"]

#####################
# DEVELOPMENT STAGE #
#####################

FROM base AS dev

ENV NEXT_PUBLIC_ENVIRONMENT=dev

VOLUME [ "/app" ]

CMD ["dev"]