const rateLimit = require('express-rate-limit');

// Giới hạn API Đăng ký: 5 lần / 1 giờ / 1 IP
const registerLimiter = rateLimit({
    windowMs: 60 * 60 * 1000, // 1 giờ (tính bằng milliseconds)
    max: 5, // Tối đa 5 request
    message: {
        success: false,
        message: "Bạn đã đăng ký quá nhiều lần. Vui lòng thử lại sau 1 giờ!"
    },
    standardHeaders: true, // Trả về thông tin rate limit ở header (RateLimit-*)
    legacyHeaders: false, // Vô hiệu hóa header cũ (X-RateLimit-*)

    // THÊM ĐOẠN NÀY ĐỂ BẮT VÀ IN LOG IP
    handler: (req, res, next, options) => {
        // Lấy IP của người dùng gửi request
        const clientIp = req.ip || req.connection.remoteAddress;
        
        // In cảnh báo đỏ ra Terminal của server
        console.warn(`[🚨 CẢNH BÁO SPAM] Bắt quả tang IP: ${clientIp} đang spam API Đăng ký!`);
        
        // Vẫn phải trả về response lỗi 429 cho Client như bình thường
        res.status(options.statusCode).json(options.message);
    }
});

module.exports = {
    registerLimiter
};