# Open Market Project

This is an Open Market project, inspired by a Market system from a game (Tibia Server) that I played.
In this market, users can interact with items and perform various actions, such as:

- Listing items for sale
- Removing listings
- Trading items
- Making and receiving offers on items
- Applying filters
- And other features

## Technologies

- Java 17
- Spring Boot
- Spring Cloud (Gateway, Discovery)
- Keycloak
- RabbitMQ
- Postgres
- MongoDB
- Docker

## Services

### Custom user storage

This service is a complement to used authentication service (Keycloak), it is made to work with a custom database (in
our case, postgres) as the main data source.

### Users

Service to handle users operations, it is the source of users data, registering, etc.

### Items

Service to handle Items operations, mainly used by admins, this service is made to create items, update items and
categories.

### Wallet

Service to handle users wallet, this is the service that receive operations with wallet and show to the user their
current balance, operations listing, etc.

### Market

Service to handle items sale, this is the service that handle all market operations, such as: insert item for sale,
remove, list items on sale, make offers, and other things related to market.

## Features

- [x] User Account Management
    - [x] Login
    - [x] Register
    - [x] Forgot Password
    - [x] Update Profile
    - [x] OAuth2
    - [x] User Profile Features

- [x] User Wallet Management
    - [x] Check informations (last updated at, balance, etc)
    - [x] List ledgers
    - [x] Make operations (transaction, withdrawal, transfer, deposit)

- [x] Items (Item, as admin)
    - [x] Create items
    - [x] Disable items
    - [x] Basic item informations
    - [x] Set base price
    - [x] Update Item
    - [x] Upload item images

- [x] Essential Search & Filters (Item)
    - [x] Search by name
    - [x] Basic category filters
    - [x] Price range filters
    - [x] Simple sort options

- [x] Advanced Item Management (Item on sale)
    - [x] List items for sale
    - [x] Remove listings
    - [x] Scheduled listings/removals

- [ ] Market core (Item on sale)
    - [x] Buy Item on sale
    - [x] Insert items for sale
    - [x] Remove items for sale

- [x] Essential Search & Filters (Item on sale)
    - [x] Search by name
    - [x] Price range filters
    - [x] Simple sort options
    - [x] Multiple criteria search
    - [x] Category filtering

- [ ] Basic Trading (Item on sale)
    - [x] Make offers
    - [x] Remove offers
    - [x] Edit offers
    - [x] Accept/Reject offers
    - [ ] View trade history
    - [x] List Offers made to item on sale

- [ ] Enhanced Trading Features (Item on sale)
    - [ ] Counter-offers
    - [ ] Notification system on trading operations
    - [x] Receive items/balance on operation finishing

- [ ] Market Analysis (Item on sale)
    - [ ] Price history tracking
    - [ ] Market trends
    - [ ] Popular items tracking
    - [ ] Price alerts

- [ ] Economic Features (Item on sale)
    - [x] Tax system
    - [x] Fee structure