DROP table IF EXISTS PUBLIC.users cascade;
DROP table IF EXISTS PUBLIC.items cascade;
DROP table IF EXISTS PUBLIC.bookings cascade;
DROP table IF EXISTS PUBLIC.comments cascade;
drop table IF EXISTS PUBLIC.items_comments cascade;
drop table IF EXISTS PUBLIC.requests cascade;
drop table IF EXISTS PUBLIC.requests_items cascade;
CREATE TABLE IF NOT EXISTS PUBLIC.users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name varchar(300),
    email varchar(320) UNIQUE);
CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description varchar(3000),
    requestor_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_requests_to_users FOREIGN KEY(requestor_id) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_name varchar(300),
    description varchar(3000),
    is_available bit,
    owner_id BIGINT,
    request_id BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id),
    CONSTRAINT fk_items_to_requests FOREIGN KEY(request_id) REFERENCES requests(id));

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT,
    booker_id BIGINT,
    status varchar(300),
    CONSTRAINT fk_bookings_to_items FOREIGN KEY(item_id) REFERENCES items(id),
    CONSTRAINT fk_bookings_to_users FOREIGN KEY(booker_id) REFERENCES users(id));



CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text varchar(3000),
    item_id BIGINT,
    author_id BIGINT,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY(author_id) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS requests_items(
    item_request_id BIGINT not null,
    items_id BIGINT not null,
    CONSTRAINT fk_requests_items_to_items FOREIGN KEY(items_id) REFERENCES items(id),
    CONSTRAINT fk_requests_items_to_requests FOREIGN KEY(item_request_id) REFERENCES requests(id));
