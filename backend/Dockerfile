# Bước 1: Build stage
FROM node:20-alpine AS build

WORKDIR /app

# Chỉ copy file package trước để tận dụng Docker cache
COPY package*.json ./
RUN npm install --only=production

# Copy toàn bộ code
COPY . .

# Bước 2: Run stage (Giúp image nhẹ nhất có thể)
FROM node:20-alpine

WORKDIR /app

# Cài đặt công cụ để kiểm tra sức khỏe container (tùy chọn)
RUN apk add --no-cache curl

# Copy từ stage build sang
COPY --from=build /app /app

# Thiết lập giới hạn bộ nhớ cho Node.js (Ví dụ: 450MB cho gói Free của Render/Docker)
ENV NODE_OPTIONS="--max-old-space-size=450"
ENV NODE_ENV=production

# Không chạy dưới quyền root để đảm bảo an toàn
USER node

EXPOSE 3000

CMD ["node", "src/app.js"]