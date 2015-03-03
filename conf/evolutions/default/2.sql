# Add dishwashers

# --- !Ups
CREATE TABLE dishwashers(
  id SERIAL NOT NULL PRIMARY KEY,
  dishwasher_id UUID,
  isClean BOOLEAN
);