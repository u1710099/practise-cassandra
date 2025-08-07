--liquibase formatted sql

--changeset gulbakhor:001-create-state_dict
CREATE TABLE IF NOT EXISTS state_dict (
                                          code TEXT,
                                          actor TEXT,
                                          lang TEXT,
                                          title TEXT,
                                          message TEXT,
                                          imageUrl TEXT,
                                          PRIMARY KEY ((code, actor), lang)
);
