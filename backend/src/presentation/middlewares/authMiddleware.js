const { verifyToken } = require('../../application/utils/jwt');

const authMiddleware = (req, res, next) => {
    try {
        const authHeader = req.headers.authorization;
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(401).json({ success: false, message: "Thiếu Token!" });
        }
        const token = authHeader.split(' ')[1];

        const decodedPayload = verifyToken(token);

        req.user = decodedPayload; 

        next();
    } catch (error) {
        return res.status(401).json({ success: false, message: "Token không hợp lệ hoặc đã hết hạn!" });
    }
};

module.exports = authMiddleware;