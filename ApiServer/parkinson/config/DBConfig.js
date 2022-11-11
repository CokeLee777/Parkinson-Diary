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
        port: process.env.MYSQL_DATABASE_PORT,
        //MYSQL BIT 타입을 JS의 BOOLEAN 타입으로 변환
        typeCast: function castField( field, useDefaultTypeCasting ) {
            if ( ( field.type === "BIT" ) && ( field.length === 1 ) ) {
                const bytes = field.buffer();
                return( bytes[ 0 ] === 1 );
            }
    
            return( useDefaultTypeCasting() );
    
        },
        pool: {
            min: 0,
            max: 10
        }
    }
});

module.exports = knex;