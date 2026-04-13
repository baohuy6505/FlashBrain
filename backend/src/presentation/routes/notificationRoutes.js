const express = require('express');
const router = express.Router();
const notificationController = require('../controllers/notificationController');
const authMiddleware = require('../middlewares/authMiddleware');

// POST /api/notifications/setting
router.post('/setting', authMiddleware, notificationController.setDailyReminder.bind(notificationController));

// GET /api/notifications/setting
router.get('/setting', authMiddleware, notificationController.getMySetting.bind(notificationController));
module.exports = router;