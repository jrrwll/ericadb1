package org.ericadb.first.syntax;

import org.ericadb.first.TestBase;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2022-02-03
 */
 class DefinitionAnalyzerTest extends TestBase  {

    @Test
    void createTable() {
        analyse("create table will.jerry (\n"
                + "    id         bigint      not null auto_increment comment 'id of the table',\n"
                + "    created_at timestamp   not null default current_timestamp,\n"
                + "    updated_at timestamp   not null default current_timestamp on update current_timestamp,\n"
                + "    `code`     varchar(63) not null comment 'some code',\n"
                + "    `name`     varchar(63) not null default 'John Doe' comment 'some name',\n"
                + "    word       text        null     default 'love',\n"
                + "    `age`      smallint(6) not null comment 'the age',\n"
                + "    deleted    bit(1)      null     default b'0',\n"
                + "    primary key (id),\n"
                + "    unique index `uk_code` (code),\n"
                + "    index ix_created_at (`created_at`),\n"
                + "    index `ix_updated_at` (`updated_at`),\n"
                + "    key ix_name_word (name, `word`)\n"
                + ") engine = innodb default charset = utf8mb4 comment ='some table';\n");
    }

    @Test
    void dropTable() {
        analyse("drop table jerry");
        analyse("drop table will.jerry;");
        analyse("drop table jerry;;");

        analyse("drop table if exists will.jerry");
        analyse("drop table if exists will.jerry;");
        analyse("drop table if exists will.jerry;;");
    }

    @Test
    void database() {
        analyse("create database will");
        analyse("create database will;");
        analyse("create database will;;");

        analyse("create database if not exists will");
        analyse("create database if not exists will;");
        analyse("create database if not exists will;;");

        analyse("drop database will");
        analyse("drop database will;");
        analyse("drop database will;;");

        analyse("drop database if exists will");
        analyse("drop database if exists will;");
        analyse("drop database if exists will;;");
    }
}
