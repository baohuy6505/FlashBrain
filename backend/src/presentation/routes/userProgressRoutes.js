const express = require('express');
const router = express.Router();
const userProgressController = require('../controllers/userProgressController');
const authMiddleware = require('../middlewares/authMiddleware');

// GET /api/progress/me - Xem tiến độ hiện tại (Streak đang là bao nhiêu)
router.get('/me', authMiddleware, userProgressController.getMyProgress.bind(userProgressController));

// POST /api/progress/study - Gọi API này mỗi khi User bấm vào học một thẻ bài / bộ bài
router.post('/study', authMiddleware, userProgressController.markStudyToday.bind(userProgressController));

module.exports = router;