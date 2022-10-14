require('dotenv').config();

const knex = require('knex').knex({
    client: 'mysql2',
    connection: {
        host: process.env.AWS_RDS_HOST,
        port: process.env.AWS_RDS_PORT,
        user: process.env.AWS_RDS_USER,
        password: process.env.AWS_RDS_PASSWORD,
        database: process.env.AWS_RDS_DATABASE
    }
});

module.exports = knex;