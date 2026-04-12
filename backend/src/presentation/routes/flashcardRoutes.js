const express = require('express');
const router = express.Router();
const flashcardController = require('../controllers/flashcardController');
const authMiddleware = require('../middlewares/authMiddleware');

// POST /api/flashcards
router.post('/', authMiddleware, flashcardController.createFlashcard.bind(flashcardController));

// GET /api/flashcards/deck/:deckId
router.get('/deck/:deckId', authMiddleware, flashcardController.getFlashcardsByDeck.bind(flashcardController));

// PUT /api/flashcards/:id
router.put('/:id', authMiddleware, flashcardController.updateFlashcard.bind(flashcardController));

// Xóa tạm thời (Soft Delete)
router.delete('/:id', authMiddleware, flashcardController.softDelete.bind(flashcardController));

// Khôi phục (Restore)
router.patch('/:id/restore', authMiddleware, flashcardController.restore.bind(flashcardController));

// Xóa vĩnh viễn (Hard Delete)
router.delete('/:id/hard', authMiddleware, flashcardController.hardDelete.bind(flashcardController));

module.exports = router;