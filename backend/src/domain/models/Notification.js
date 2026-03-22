const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
  _id: { type: String, required: true },
  user_id: { type: String, ref: 'User', required: true },
  
  title: { type: String, required: true },
  message: { type: String, required: true },
  
  scheduled_at: { type: Date, required: true },
  is_sent: { type: Boolean, default: false }
}, { 
  timestamps: { createdAt: 'created_at', updatedAt: false } 
});

notificationSchema.index({ is_sent: 1, scheduled_at: 1 });

module.exports = mongoose.model('Notification', notificationSchema);