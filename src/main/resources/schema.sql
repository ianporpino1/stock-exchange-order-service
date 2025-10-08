CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS trade_order (
    order_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    symbol VARCHAR(10) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('BUY', 'SELL')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACCEPTED', 'PARTIALLY_EXECUTED', 'TOTALLY_EXECUTED', 'REJECTED', 'PENDING')),
    price NUMERIC(19, 4) NOT NULL,
    executed_quantity INT NOT NULL DEFAULT 0,
    total_quantity INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id UUID NOT NULL
    );

CREATE TABLE IF NOT EXISTS trade (
    trade_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    buy_order_id UUID NOT NULL REFERENCES trade_order(order_id),
    sell_order_id UUID NOT NULL REFERENCES trade_order(order_id),
    buyer_user_id UUID NOT NULL,
    seller_user_id UUID NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC(19, 4) NOT NULL,
    executed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_trade_order_status ON trade_order(status);
CREATE INDEX IF NOT EXISTS idx_trade_order_user_id ON trade_order(user_id);