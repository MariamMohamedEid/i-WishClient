# 🎁 i-Wish Desktop Application

## Overview

**i-Wish** is a multi-threaded client-server desktop application that enables users to create and share wish lists, connect with friends, and contribute to fulfilling each other's wishes through a joyful, gamified experience. It uses Oracle Database for persistent storage and employs a thread pool on the server side for handling concurrent client requests efficiently.

---

## 🧠 Abstract

i-Wish enhances the gifting process through a social and interactive experience:
- Manage personal and friends’ wish lists.
- Send and respond to friend requests.
- Contribute toward fulfilling items on wish lists, including **group gifting**.
- Receive real-time notifications about gift status.
- Friendly GUI to maximize user enjoyment.

All operations are backed by a structured Oracle database and fully validated across the service layers.

---

## 🧱 Architecture

### 💻 Client (JavaFX)
Core Features:
- **Authentication**: Sign-up, login
- **Friend Management**: Add/remove friends, view friend lists, handle friend requests
- **Wishlist Features**: Create/update/delete wishes, view friends’ wish lists
- **Contributions**: Top-up (simulated), contribute partial or full amounts
- **Group Gifting**: Multiple users can contribute toward a single item until it’s fully funded
- **Notifications**: Receive messages about contributions and completed gifts
- **GUI**: Intuitive and friendly interface

#### 📦 Client Packages & Classes
- `wishclient`, `wishclienthandler`: Handle communication with the server
- `pkg.dto`:
  - `user`, `newuser`, `currentuser`: Represent user data
  - `wish`, `item`: Wish list item structures
  - `notification`: Messages received by the client
- `login`, `signup`: GUI for authentication
- `addfriend`, `friend`, `friendrequest`, `friendwishlist`: Friend interaction components
- `notification`: Real-time updates for the user
- `changepoints`: Handles user credits

---

### 🖥 Server (Threaded Java App)
Server Responsibilities:
- Manage all incoming client connections via a thread pool
- Validate, parse, and route client commands
- Interface with the Oracle database through DAOs
- Broadcast notifications to appropriate clients
- Handle **group gifting**: aggregate contributions and notify all participants upon completion

#### 📦 Server Packages & Classes
- `wishserver`, `wishserverhandler`: Entry point and thread-handling logic
- `pkg.dal`:
  - `userAO`, `friendAO`, `friendrequestAO`, `notificationAO`, `productAO`, `wishesAO`: DAO classes for Oracle interaction
- `pkg.dto`: Shared data model classes (`user`, `wish`, `item`, `notification`, `newuser`, `currentuser`)
- `pkg.servicelayer`:
  - `userrequest`, `friendrequest`, `notificationrequest`, `wishrequest`, `itemrequest`: Business logic and processing

---

## ⚙️ Technologies Used

| Tech                 | Purpose                                  |
|----------------------|------------------------------------------|
| Java / JavaFX        | GUI and application logic                |
| Oracle DB            | Persistent backend storage               |
| Sockets + ThreadPool | Concurrent client-server communication   |
| DTO Pattern          | Structured data transfer between layers  |

---

## 🚀 Getting Started

### Requirements
- Java 8+
- Oracle DB (configured with user/table schema)
- JavaFX (or compatible GUI library)

### Run Instructions

1. **Start the Server**
   ```bash
   java wishserver.WishServer


2. **Launch the Client**

   ```bash
   java wishclient.WishClient
   ```

---

## 🔔 Core Features Map

| Feature                          | Client Class/Module      | Server Class/Layer                   |
| -------------------------------- | ------------------------ | ------------------------------------ |
| User Registration / Login        | `signup`, `login`        | `userrequest`, `userAO`              |
| Add/Remove Friend                | `addfriend`, `friend`    | `friendrequest`, `friendAO`          |
| Manage Friend Requests           | `friendrequest`          | `friendrequestAO`                    |
| View Friends and Wish Lists      | `friendwishlist`, `wish` | `wishesAO`, `wishrequest`            |
| Wishlist Operations              | `item`, `wish`           | `itemrequest`, `productAO`           |
| Contribution (Simulated Payment) | `changepoints`           | `userAO`, `notificationAO`           |
| **Group Gifting**                | `changepoints`, `wish`   | `wishrequest`, `notificationrequest` |
| Notifications (Giver/Receiver)   | `notification`           | `notificationrequest`                |

---

## 🔮 Future Work

* Integrate product search via Amazon or eBay APIs
* Add admin dashboard for item moderation and analytics
* Enable real payment APIs (e.g., Stripe test mode)
* Advanced search and filtering in friend/item views

---

## 👥 Contributors

- **Ahmed Otifi** 🔗 [https://github.com/otifi3](https://github.com/otifi3)  
- **Doaa Reda** 🔗 [https://github.com/doaaredaa](https://github.com/doaaredaa)  
- **Mariam Eid** 🔗 [https://github.com/MariamMohamedEid](https://github.com/MariamMohamedEid)
- **Yomna Afifi** 🔗 [https://github.com/yomnaafifi](https://github.com/yomnaafifi)

