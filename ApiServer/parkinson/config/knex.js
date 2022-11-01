const path = require('path');
require('dotenv').config({ 
    path: path.resolve(__dirname, `../env/${process.env.NODE_ENV}.env`)
});

const knex = require('knex').knex({
    client: 'mysql2',
    connection: {
        host: process.env.MYSQL_HOST,
        user: process.env.MYSQL_ROOT_USERNAME,
        password: process.env.MYSQL_ROOT_PASSWORD,
        database: process.env.MYSQL_DATABASE,
        port: process.env.MYSQL_DATABASE_PORT
    }
});

module.exports = knex;