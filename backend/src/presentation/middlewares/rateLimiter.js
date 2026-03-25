const rateLimit = require('express-rate-limit');

const registerLimiter = rateLimit({
    windowMs: 60 * 60 * 1000, 
    max: 5, 
    message: {
        success: false,
        message: "Bạn đã đăng ký quá nhiều lần. Vui lòng thử lại sau 1 giờ!"
    },
    standardHeaders: true,
    legacyHeaders: false, 

    
    handler: (req, res, next, options) => {
        const clientIp = req.ip || req.connection.remoteAddress;
        
        console.warn(`[🚨 CẢNH BÁO SPAM] Bắt quả tang IP: ${clientIp} đang spam API Đăng ký!`);
        
        res.status(options.statusCode).json(options.message);
    }
});

module.exports = {
    registerLimiter
};