CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100),
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20),
                       status VARCHAR(30) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          account_number VARCHAR(50) NOT NULL UNIQUE,
                          account_type VARCHAR(30) NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          available_balance NUMERIC(19, 4) NOT NULL DEFAULT 0,
                          blocked_balance NUMERIC(19, 4) NOT NULL DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          version BIGINT NOT NULL DEFAULT 0,

                          CONSTRAINT fk_accounts_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
);

CREATE TABLE instruments (
                             id BIGSERIAL PRIMARY KEY,
                             symbol VARCHAR(30) NOT NULL UNIQUE,
                             name VARCHAR(150) NOT NULL,
                             instrument_type VARCHAR(30) NOT NULL,
                             current_price NUMERIC(19, 4),
                             status VARCHAR(30) NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        account_id BIGINT NOT NULL,
                        instrument_id BIGINT NOT NULL,
                        order_type VARCHAR(30) NOT NULL,
                        order_side VARCHAR(20) NOT NULL,
                        quantity NUMERIC(19, 4) NOT NULL,
                        price NUMERIC(19, 4),
                        filled_quantity NUMERIC(19, 4) NOT NULL DEFAULT 0,
                        status VARCHAR(30) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        version BIGINT NOT NULL DEFAULT 0,

                        CONSTRAINT fk_orders_account
                            FOREIGN KEY (account_id)
                                REFERENCES accounts(id),

                        CONSTRAINT fk_orders_instrument
                            FOREIGN KEY (instrument_id)
                                REFERENCES instruments(id),

                        CONSTRAINT chk_orders_quantity
                            CHECK (quantity > 0),

                        CONSTRAINT chk_orders_price
                            CHECK (price IS NULL OR price > 0)
);

CREATE TABLE trades (
                        id BIGSERIAL PRIMARY KEY,
                        buy_order_id BIGINT NOT NULL,
                        sell_order_id BIGINT NOT NULL,
                        instrument_id BIGINT NOT NULL,
                        quantity NUMERIC(19, 4) NOT NULL,
                        price NUMERIC(19, 4) NOT NULL,
                        total_amount NUMERIC(19, 4) NOT NULL,
                        executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_trades_buy_order
                            FOREIGN KEY (buy_order_id)
                                REFERENCES orders(id),

                        CONSTRAINT fk_trades_sell_order
                            FOREIGN KEY (sell_order_id)
                                REFERENCES orders(id),

                        CONSTRAINT fk_trades_instrument
                            FOREIGN KEY (instrument_id)
                                REFERENCES instruments(id),

                        CONSTRAINT chk_trades_quantity
                            CHECK (quantity > 0),

                        CONSTRAINT chk_trades_price
                            CHECK (price > 0)
);

CREATE TABLE holdings (
                          id BIGSERIAL PRIMARY KEY,
                          account_id BIGINT NOT NULL,
                          instrument_id BIGINT NOT NULL,
                          quantity NUMERIC(19, 4) NOT NULL DEFAULT 0,
                          average_price NUMERIC(19, 4) NOT NULL DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          version BIGINT NOT NULL DEFAULT 0,

                          CONSTRAINT fk_holdings_account
                              FOREIGN KEY (account_id)
                                  REFERENCES accounts(id),

                          CONSTRAINT fk_holdings_instrument
                              FOREIGN KEY (instrument_id)
                                  REFERENCES instruments(id),

                          CONSTRAINT uk_holdings_account_instrument
                              UNIQUE (account_id, instrument_id)
);

CREATE TABLE wallet_transactions (
                                     id BIGSERIAL PRIMARY KEY,
                                     account_id BIGINT NOT NULL,
                                     transaction_type VARCHAR(30) NOT NULL,
                                     amount NUMERIC(19, 4) NOT NULL,
                                     status VARCHAR(30) NOT NULL,
                                     reference_id VARCHAR(100),
                                     description VARCHAR(255),
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_wallet_transactions_account
                                         FOREIGN KEY (account_id)
                                             REFERENCES accounts(id),

                                     CONSTRAINT chk_wallet_transaction_amount
                                         CHECK (amount > 0)
);