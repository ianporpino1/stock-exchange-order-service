INSERT INTO ticker (symbol, last_price) VALUES ('AAPL', 200.00) ON CONFLICT (symbol) DO NOTHING;
INSERT INTO ticker (symbol, last_price) VALUES ('MSFT', 150.00) ON CONFLICT (symbol) DO NOTHING;
INSERT INTO ticker (symbol, last_price) VALUES ('GOOGL', 100.00) ON CONFLICT (symbol) DO NOTHING;
