const express = require('express');
const router = express.Router();
const deckController = require('../controllers/deckController');

// GET /api/decks
router.get('/', deckController.getAllDecksByUserId);

// Lấy chi tiết một bộ thẻ cụ thể
// GET /api/decks/:id
router.get('/:id', deckController.getDeckById.bind(deckController));

// Tạo mới một bộ thẻ
// POST /api/decks
router.post('/', deckController.createDeck.bind(deckController));

// Cập nhật thông tin bộ thẻ
// PUT /api/decks/:id
router.put('/:id', deckController.updateDeck.bind(deckController));

// Xóa tạm thời (Cho vào thùng rác)
// DELETE /api/decks/:id
router.delete('/:id', deckController.softDeleteDeck.bind(deckController));

// Khôi phục bộ thẻ từ thùng rác
// PATCH /api/decks/:id/restore
router.patch('/:id/restore', deckController.restoreDeck.bind(deckController));

// Xóa vĩnh viễn khỏi Database
// DELETE /api/decks/:id/hard
router.delete('/:id/hard', deckController.hardDeleteDeck.bind(deckController));

module.exports = router;