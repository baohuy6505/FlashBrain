const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
require("dotenv").config();
const connectDB = require('./infrastructure/database/dbConnect');
const cloudinary = require('./infrastructure/database/cloudinary')
const app = express();
const routes = require('./presentation/routes');

app.use(cors());
app.use(express.json());

//Mock routes
app.use('/api', routes);

app.use(express.json());

// Connect to MongoDB Atlas
connectDB();
cloudinary();

// test route
app.get("/test-router", (req, res) => {
    res.send("FlashBrain API running...");
});

const PORT = 3000;
app.listen(PORT, "0.0.0.0", () => {
    console.log(`🔥 Server is running!`);
    console.log(`Local: http://localhost:${PORT}`);
    // In ra địa chỉ IP thật để Huy dễ copy
    console.log(`Network: http://192.168.1.47:${PORT}`); 
});

// const PORT = 3000;
// app.listen(PORT, () => {
//     console.log(`🔥 Server is running in mode: ${process.env.NODE_ENV || 'development'}`);
//     console.log(`Server running on port http://localhost:${PORT}`);
// });