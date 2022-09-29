const mysql = require('mysql2');

const db = mysql.createConnection({
    host: `${process.env.AWS_RDS_HOST}`,
    user: `${process.env.AWS_RDS_USER}`,
    password: `${process.env.AWS_RDS_PASSWORD}`,
    database: `${process.env.AWS_RDS_DATABASE}`,
    port: `${process.env.AWS_RDS_PORT}`
});
db.connect();

module.exports = db;


