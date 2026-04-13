const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
  _id: { type: String, required: true },
  user_id: { type: String, ref: 'User', required: true, unique: true }, // THÊM unique: true
  
  title: { type: String, required: true },
  message: { type: String, required: true },
  
  daily_time: { type: String, required: true }, // Lưu String "20:00" thay vì Date
  is_active: { type: Boolean, default: true }   // Bật/Tắt nhận thông báo
}, { 
  // NÊN bật updatedAt để biết user đổi giờ thông báo lần cuối khi nào
  timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' }
});

notificationSchema.index({ is_active: 1, daily_time: 1 });

module.exports = mongoose.model('Notification', notificationSchema);