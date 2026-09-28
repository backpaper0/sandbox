import base64
import json
import logging
import os
from pathlib import Path
from urllib.request import Request, urlopen

logging.basicConfig(level=logging.INFO)

logger = logging.getLogger(__name__)


class RequestError(Exception):
    pass


nexus_url = os.environ["NEXUS_URL"]
admin_password = Path("/run/secrets/password").read_text().strip()
credentials = base64.b64encode(f"admin:{admin_password}".encode()).decode()


def process_admin_password():
    password_file = Path("/nexus-data/admin.password")

    if not password_file.exists():
        logger.info("Nexus admin password is already initialized.")
        return

    initial_password = password_file.read_text().strip()

    logger.info("Changing Nexus admin password...")

    initial_credentials = base64.b64encode(
        f"admin:{initial_password}".encode()
    ).decode()

    req = Request(
        method="PUT",
        url=f"{nexus_url}/service/rest/v1/security/users/admin/change-password",
        headers={
            "Authorization": f"Basic {initial_credentials}",
            "Content-Type": "text/plain",
        },
        data=admin_password.encode(),
    )

    with urlopen(req) as response:
        status = response.status
        if not 200 <= status < 300:
            raise RequestError(f"Unexpected HTTP status: {status}")

    logger.info("Nexus admin password initialized successfully.")


def process_eula():
    logger.info("Accepting eula...")

    req = Request(
        method="GET",
        url=f"{nexus_url}/service/rest/v1/system/eula",
        headers={
            "Authorization": f"Basic {credentials}",
        },
    )

    with urlopen(req) as response:
        status = response.status
        if not 200 <= status < 300:
            raise RequestError(f"Unexpected HTTP status: {status}")
        data = json.loads(response.read().decode())

    if data["accepted"]:
        logger.info("Already accepted")
        return

    data["accepted"] = True

    req = Request(
        method="POST",
        url=f"{nexus_url}/service/rest/v1/system/eula",
        headers={
            "Authorization": f"Basic {credentials}",
            "Content-Type": "application/json",
        },
        data=json.dumps(data).encode(),
    )

    with urlopen(req) as response:
        status = response.status
        if not 200 <= status < 300:
            raise RequestError(f"Unexpected HTTP status: {status}")

    logger.info("Accept eula")


def process_anonymous_access():
    logger.info("Anonymous access configure...")

    req = Request(
        method="GET",
        url=f"{nexus_url}/service/rest/v1/security/anonymous",
        headers={
            "Authorization": f"Basic {credentials}",
        },
    )

    with urlopen(req) as response:
        status = response.status
        if not 200 <= status < 300:
            raise RequestError(f"Unexpected HTTP status: {status}")
        data = json.loads(response.read().decode())

    if data["enabled"]:
        logger.info("Already enabled")
        return

    data["enabled"] = True

    req = Request(
        method="PUT",
        url=f"{nexus_url}/service/rest/v1/security/anonymous",
        headers={
            "Authorization": f"Basic {credentials}",
            "Content-Type": "application/json",
        },
        data=json.dumps(data).encode(),
    )

    with urlopen(req) as response:
        status = response.status
        if not 200 <= status < 300:
            raise RequestError(f"Unexpected HTTP status: {status}")

    logger.info("Enable anonymous access")


DEFAULT_STORAGE = {
    "blobStoreName": "default",
    "strictContentTypeValidation": True,
}

HOSTED_STORAGE = DEFAULT_STORAGE | {"writePolicy": "ALLOW"}

DOCKER_HOSTED_PORT = 5000
DOCKER_GROUP_PORT = 5001

REPOSITORIES = [
    (
        "docker/hosted",
        {
            "name": "docker-hosted",
            "online": True,
            "storage": HOSTED_STORAGE,
            "docker": {
                "v1Enabled": False,
                "forceBasicAuth": False,
                "httpPort": DOCKER_HOSTED_PORT,
            },
        },
    ),
    (
        "docker/proxy",
        {
            "name": "docker-proxy",
            "online": True,
            "storage": DEFAULT_STORAGE,
            "proxy": {
                "remoteUrl": "https://registry-1.docker.io",
                "contentMaxAge": 1440,
                "metadataMaxAge": 1440,
            },
            "negativeCache": {
                "enabled": True,
                "timeToLive": 1440,
            },
            "httpClient": {
                "blocked": False,
                "autoBlock": True,
            },
            "docker": {
                "v1Enabled": False,
                "forceBasicAuth": False,
            },
            "dockerProxy": {
                "indexType": "HUB",
                "cacheForeignLayers": False,
                "foreignLayerUrlWhitelist": [],
            },
        },
    ),
    (
        "docker/group",
        {
            "name": "docker-group",
            "online": True,
            "storage": DEFAULT_STORAGE,
            "group": {
                "memberNames": ["docker-hosted", "docker-proxy"],
            },
            "docker": {
                "v1Enabled": False,
                "forceBasicAuth": False,
                "httpPort": DOCKER_GROUP_PORT,
            },
        },
    ),
    (
        "raw/hosted",
        {
            "name": "raw-hosted",
            "online": True,
            "storage": HOSTED_STORAGE,
            "raw": {
                "contentDisposition": "ATTACHMENT",
            },
        },
    ),
]


def get_repository_names():
    req = Request(
        method="GET",
        url=f"{nexus_url}/service/rest/v1/repositories",
        headers={
            "Authorization": f"Basic {credentials}",
        },
    )

    with urlopen(req) as response:
        status = response.status
        if not 200 <= status < 300:
            raise RequestError(f"Unexpected HTTP status: {status}")
        data = json.loads(response.read().decode())

    return {repository["name"] for repository in data}


def process_repositories():
    logger.info("Creating repositories...")

    existing_names = get_repository_names()

    for recipe, repository in REPOSITORIES:
        name = repository["name"]

        if name in existing_names:
            logger.info("Repository %s already exists", name)
            continue

        req = Request(
            method="POST",
            url=f"{nexus_url}/service/rest/v1/repositories/{recipe}",
            headers={
                "Authorization": f"Basic {credentials}",
                "Content-Type": "application/json",
            },
            data=json.dumps(repository).encode(),
        )

        with urlopen(req) as response:
            status = response.status
            if not 200 <= status < 300:
                raise RequestError(f"Unexpected HTTP status: {status}")

        logger.info("Created repository %s (%s)", name, recipe)


def main():
    process_admin_password()
    process_eula()
    process_anonymous_access()
    process_repositories()


if __name__ == "__main__":
    try:
        main()
    except BaseException:
        logger.exception("Something went wrong")
