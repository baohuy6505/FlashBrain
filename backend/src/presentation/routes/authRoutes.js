const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

const { registerLimiter } = require('../middlewares/rateLimiter');

router.post('/register', registerLimiter, authController.register);
router.post('/login', authController.login);

module.exports = router;