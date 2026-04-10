const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

const upload = require('../middlewares/uploadMiddleware');
const { registerLimiter } = require('../middlewares/rateLimiter');
const authMiddleware = require('../middlewares/authMiddleware');

// 1. Đăng ký tài khoản mới
// POST /api/auth/register
router.post('/register', registerLimiter, authController.register);
// 2. Đăng nhập
// POST /api/auth/login
router.post('/login', authController.login);

// 3. Đăng xuất
// POST /api/auth/logout
router.post('/logout', authMiddleware, authController.logout);

// 4. Xem thông tin cá nhân 
//GET /api/auth/me
router.get('/me', authMiddleware, authController.getMyProfile);

// 5. Cập nhật thông tin cá nhân 
// PUT /api/auth/me
router.put('/me', authMiddleware, upload.single('image'), authController.updateMyProfile);

//6. Đổi mật khẩu
// PUT /api/auth/me/password
router.put('/me/password', authMiddleware, authController.changeMyPassword);

//7. Xóa tài khoản
// DELETE /api/auth/me
router.delete('/me', authMiddleware, authController.deleteMyAccount);

module.exports = router;