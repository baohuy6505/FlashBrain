const authService = require('../../application/services/authService');

class AuthController {
    async register(req, res) {
        try {
            if (!req.body.email || !req.body.password) {
                return res.status(400).json({ success: false, message: "Thiếu email hoặc password" });
            }
            const result = await authService.register(req.body);
            return res.status(201).json({ success: true, message: "Đăng ký thành công", data: result });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    async login(req, res) {
        try {
            if (!req.body.email || !req.body.password) {
                return res.status(400).json({ success: false, message: "Thiếu email hoặc password" });
            }
            const result = await authService.login(req.body);
            return res.status(200).json({ success: true, message: "Đăng nhập thành công", data: result });
        } catch (error) {
            return res.status(401).json({ success: false, message: error.message });
        }
    }
}
module.exports = new AuthController();