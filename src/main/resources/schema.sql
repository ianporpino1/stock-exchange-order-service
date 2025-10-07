CREATE TABLE IF NOT EXISTS trade_order (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    symbol VARCHAR(10) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    price NUMERIC(19, 4) NOT NULL,
    executed_quantity INT NOT NULL,
    total_quantity INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    user_id UUID NOT NULL
    );

CREATE TABLE IF NOT EXISTS trade (
    trade_id UUID PRIMARY KEY,
    buy_order_id UUID NOT NULL,
    sell_order_id UUID NOT NULL,
    buyer_user_id UUID NOT NULL,
    seller_user_id UUID NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC(19, 4) NOT NULL,
    executed_at TIMESTAMPTZ NOT NULL
    );