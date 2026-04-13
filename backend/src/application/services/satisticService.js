const ISatisticService = require('../interfaces/ISatisticService')
const DeckRepository = require('../../infrastructure/repositories/deckRepository');
const flashcardRepository = require('../../infrastructure/repositories/flashcardRepository');
const UserStatsResponseDto = require('../dtos/authDto/userStatsResponseDto');

const deckRepository = new DeckRepository();

class StatisticService extends ISatisticService {
    async getUserDashboardStats(userId) {
        const decks = await deckRepository.findAll(userId);
        const totalDecks = decks.length;

        if (totalDecks === 0) {
            return new UserStatsResponseDto({ totalDecks: 0 });
        }

        const deckIds = decks.map(deck => deck._id.toString());

        const cardStats = await flashcardRepository.getStatsByDeckIds(deckIds);

        return new UserStatsResponseDto({
            totalDecks: totalDecks,
            totalCards: cardStats.totalCards,
            cardsToReview: cardStats.cardsToReview,
            newCards: cardStats.newCards,
            learnedCards: cardStats.learnedCards
        });
    }
}

module.exports = new StatisticService();