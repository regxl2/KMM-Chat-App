[//]: # (This is a Kotlin Multiplatform project targeting Android, iOS.)

[//]: # ()
[//]: # (* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.)

[//]: # (  It contains several subfolders:)

[//]: # (  - `commonMain` is for code that’s common for all targets.)

[//]: # (  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.)

[//]: # (    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,)

[//]: # (    `iosMain` would be the right folder for such calls.)

[//]: # ()
[//]: # (* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, )

[//]: # (  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.)

[//]: # ()
[//]: # (* `/shared` is for the code that will be shared between all targets in the project.)

[//]: # (  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.)

# Chat Application

A modern chat application built using Kotlin Multiplatform for the frontend (Android & iOS) and TypeScript for the backend.

## Project Structure

```
├── composeApp/               # Shared Compose Multiplatform code
│   ├── commonMain/          # Common code for all platforms
│   ├── androidMain/         # Android-specific code
│   └── iosMain/            # iOS-specific code
│
├── iosApp/                  # iOS application entry point
│   └── SwiftUI code
│
├── shared/                  # Shared KMM code
│   ├── commonMain/         # Common code for all platforms
│   ├── androidMain/        # Android-specific code
│   └── iosMain/           # iOS-specific code
│
└── Chat-App-Backend/        # Backend server implementation
    ├── api-server/         # REST API Server
    └── websocket-server/   # WebSocket Server
```

### Frontend (Kotlin Multiplatform)

### `/composeApp`
Contains shared code for Compose Multiplatform applications:
- **commonMain**: Shared code for all targets (Android and iOS)
- **Platform-Specific Folders**:
  - `iosMain`: iOS-specific implementations (e.g., Apple's CoreCrypto, platform APIs)
  - `androidMain`: Android-specific implementations

### `/iosApp`
iOS application entry point:
- Contains iOS-specific configurations
- Houses SwiftUI code
- Required even when sharing UI with Compose Multiplatform

### `/shared`
Core shared code between all targets:
- **commonMain**: Contains:
  - Core business logic
  - Utilities
  - Shared features
- Platform-specific folders available for specialized implementations

### Backend (`/chat-app-backend`)

The backend is built with modern technologies to provide robust real-time communication:

#### Technology Stack
- **TypeScript**: Primary programming language
- **Node.js**: Server runtime environment
- **WebSockets**: Real-time communication
  - One-to-one chat
  - Room-based communication
- **Redis**:
  - Pub/Sub messaging
- **MongoDB**: Database for storing
  - User data
  - Chat messages
  - Application state
- **Express**: RESTful API framework

## Getting Started

Refer to individual setup guides in each directory for detailed instructions:
- Backend setup: `/chat-app-backend/README.md`

## Preview of the App
- [Watch on youtube](https://youtu.be/c1qRkZNrPhs)

## Documentation

For more detailed information, refer to the official documentation:

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Node.js](https://nodejs.org/docs)
- [Redis](https://redis.io/documentation)
- [MongoDB](https://www.mongodb.com/docs/)
