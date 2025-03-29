DEPLOY_DIR="$(pwd)"
DEPLOY_FILE="$DEPLOY_DIR/deploy.txt"
IMAGE_NAME="weakwill-api"
IMAGE_TAG="latest"
CONTAINER_NAME="weakwill-api-container"
DOCKERFILE="$DEPLOY_DIR/Dockerfile"

if [ -f "$DEPLOY_FILE" ]; then
    echo "Found $DEPLOY_FILE, proceeding with Docker build..."

    # 1) Docker 이미지 빌드
    DOCKER_BUILDKIT=1 docker build -t $IMAGE_NAME:$IMAGE_TAG -f "$DOCKERFILE" "$DEPLOY_DIR"

    # 2) 기존 컨테이너 중지 & 이름 변경
    if docker ps -q -f name=$CONTAINER_NAME; then
        OLD_CONTAINER_NAME="${CONTAINER_NAME}-old-$(date +%Y%m%d%H%M%S)"
        docker stop $CONTAINER_NAME
        docker rename $CONTAINER_NAME $OLD_CONTAINER_NAME
    fi

    # 3) 새 컨테이너 실행 (8080:8080)
    docker run -d -p 8080:8080 --name $CONTAINER_NAME $IMAGE_NAME:$IMAGE_TAG

    # 4) 이전 컨테이너 삭제 (선택)
    if [ -n "$OLD_CONTAINER_NAME" ]; then
        docker rm -f $OLD_CONTAINER_NAME
    fi

    # 5) deploy 폴더 정리 (선택)
    rm -rf "$DEPLOY_DIR"/*
else
    echo "No deploy.txt found. Skipping deployment."
fi
