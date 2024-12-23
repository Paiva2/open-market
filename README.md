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
- ...

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

- [ ] Essential Search & Filters (Item on sale)
    - [ ] Search by name
    - [ ] Basic category filters
    - [ ] Price range filters
    - [ ] Simple sort options
    - [ ] Multiple criteria search
    - [ ] Advanced attribute filtering
    - [ ] Price comparison tools

- [ ] Basic Trading (Item on sale)
    - [ ] Make offers
    - [ ] Accept/Reject offers
    - [ ] Basic messaging system
    - [ ] View trade history

- [ ] Advanced Item Management (Item on sale)
    - [ ] Batch operations
    - [ ] Scheduled listings/removals
    - [ ] Advanced item descriptions
    - [ ] List items for sale
    - [ ] Remove listings

- [ ] Enhanced Trading Features (Item on sale)
    - [ ] Counter-offers
    - [ ] Offer expiration time
    - [ ] Trade-specific chat
    - [ ] Advanced notification system

- [ ] Market Analysis (Item on sale)
    - [ ] Price history tracking
    - [ ] Market trends
    - [ ] Popular items tracking
    - [ ] Price alerts

- [ ] Economic Features (Item on sale)
    - [ ] Automatic tax system
    - [ ] Advanced fee structure
    - [ ] Bulk transaction handling
    - [ ] Economic indicators
