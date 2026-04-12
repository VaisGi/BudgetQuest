# BudgetQuest: Backend Architecture PRD & Analysis

## Executive Summary
This document outlines the architectural research, evaluation, and proposed implementation plan for the **BudgetQuest Backend**. The core objective is to design a system that is **highly scalable, cost-effective, future-proof**, and optimized for **agentic (AI-assisted) vibe coding**. 

We heavily explored modern, cutting-edge solutions specifically **Bun.js** and **PostgREST**, benchmarking them against industry standards (Firebase, Node/NestJS, Go) to find the perfect paradigm for an offline-first KMP mobile application.

---

## 1. Technology Deep Dive & Analysis

### A. Bun.js (The All-in-One JS/TS Runtime)
**Overview:** Bun is a wildly fast JavaScript runtime, bundler, test runner, and package manager written in Zig. 
- **Pros (Speed & DX):** Executes TypeScript natively without transpile steps (perfect for agentic coding speed). It significantly outperforms Node.js in HTTP request handling and cold boot times.
- **Vibe Coding Suitability:** Extremely high. Using frameworks like **Elysia.js** (a Bun-first framework), you achieve out-of-the-box End-to-End Type Safety. AI models excel at generating predictable Elysia routes + Drizzle ORM schemas. 
- **Cost-Effectiveness:** Due to lower CPU and memory overhead per request compared to Node.js, it scales horizontally on bare-metal or cheap cloud configurations (like Hetzner or Fly.io) at a fraction of the cost.

### B. PostgREST / Supabase (The Database-Is-The-API Model)
**Overview:** PostgREST reads your PostgreSQL schema and instantly turns it into a fully-fledged REST API. It is the engine powering Supabase.
- **Pros (Thin Tier):** Replaces massive boilerplate API code with sheer database logic. You configure access via Row-Level Security (RLS) policies.
- **Sync Friendly:** Excellent for BudgetQuest's offline-first architecture. A thin API tier handles CRUD while keeping the heavy lifting strictly on the Postgres layer.
- **Vibe Coding Suitability:** Perfect. AI easily writes SQL schemas and RLS policies. It completely eliminates writing boilerplate controllers/services.

### C. Industry Alternatives
- **Node.js / Express / NestJS:** Bulletproof but bloated. Harder to maintain long-term in an AI context due to massive file counts, heavy configuration, and OOP dependency injection graphs (NestJS).
- **Go / Rust:** Incredible performance, but overkill for a REST/GraphQL layer. Iteration speed is slower than TypeScript, limiting "vibe coding" fluidity.
- **Firebase / Firestore:** Easy to start, but NoSQL scales poorly for strict financial ledgers/transactions. Vendor lock-in and high scaling costs make it sub-optimal.

---

## 2. Proposed Architectural Stack Recommendations

Based on the analysis, we have two elite, cost-effective architectures. 

### Option 1: The "Code-First" Stack (Bun + Elysia + Drizzle + Postgres)
_Best for complex custom business logic and heavy backend orchestration._
- **Runtime:** Bun.js
- **Framework:** Elysia.js (for high-perf, type-safe API generation)
- **Database:** PostgreSQL (hosted on Supabase, Neon, or Railway)
- **ORM:** Drizzle ORM (lightweight, strict SQL-like TS typing)
- **Why it wins:** Developer experience is 10/10. AI models generate Elysia/Drizzle code effortlessly. It runs anywhere with negligible hosting costs.

### Option 2: The "Thick-DB / Thin-API" Stack (Supabase via PostgREST)
_Best for rapid product iteration and offline-sync heavy apps._
- **Database / API:** Supabase (combines Postgres, PostgREST, Auth, and Storage)
- **Edge Functions:** Deno or Bun (for webhook handling and background tasks)
- **Why it wins:** Zero backend code required for 90% of requests. You write SQL for your schema and RLS policies, and the API is instantly ready for BudgetQuest's Ktor client to consume. 

> [!IMPORTANT]
> **Architectural Recommendation:** 
> For **BudgetQuest**, I highly recommend **Option 2 (Supabase/PostgREST)** for the data sync layer, paired with **Bun.js Edge Services** for heavy processing (like AI insights generation or receipt parsing). This yields a backend that requires virtually zero maintenance, costs literal pennies to scale, and utilizes precise, easily AI-generated SQL policies.

---

## 3. Project Requirements Document (PRD)

### Backend Goals & Requirements
1. **Offline-First Synchronization:** The backend must efficiently sync data from BudgetQuest's local SQLite database. We need Delta-Sync (only syncing changes) to preserve bandwidth.
2. **Auth & Security:** Secure JWT-based authentication. Users must only access their own Ledgers, Quests, and Budgets (handled via Postgres RLS).
3. **High Performance / Low Latency:** Sub 100ms API response times.
4. **Agentic Maintenance:** Infrastructure as Code (IaC) or simple declarative schemas (SQL + Drizzle/Prisma) so AI agents can maintain the monolith easily without human mental overhead.
5. **Real-Time Data & Push:** The backend must support active bi-directional streaming for live updates and handle both standard and silent push notifications to keep offline stores up to date automatically across devices.

### Data Model Mapping (Postgres Translation)
The backend schema will closely mirror our existing `BudgetQuestDatabase.sq`:
- `UserProfile` -> `users` table (UUID, email, preferences)
- `TransactionEntity` -> `transactions` table (with Foreign Keys to users, indices on `monthYear` for fast querying)
- `BudgetEntity` -> `budgets` table
- `QuestEntity` & `SavingsGoalEntity` -> Corresponding tables with sync timestamps (`updated_at`, `deleted_at` for soft deletes ensuring sync continuity).

### Deployment Strategy
- **Database:** Supabase Managed Postgres (generous free tier, scales brilliantly, zero setup).
- **Custom Business Logic (If needed):** Fly.io running a Dockerized Bun container or Supabase Edge Functions.

---

## 4. Live Streaming & Notifications Architecture

To provide a true premium, modern feel, BudgetQuest will utilize event-driven push architecture:

### A. Live Data Streaming (WebSockets)
- **Supabase Realtime:** Instead of aggressive HTTP polling, BudgetQuest (Android & iOS) will subscribe directly to PostgreSQL changes (INSERT, UPDATE, DELETE) over secure WebSockets via the Supabase Realtime service. When a transaction is logged on a shared budget (or multi-device), the other devices receive the payload instantly over the socket.
- **Bun.js Fallback:** If using the Custom Option 1, Elysia.js natively supports WebSockets with extreme performance, maintaining ~1M concurrent connections per basic instance.

### B. Push Notifications & Silent Push
To handle scenarios where the app is closed:
- **Event Triggers:** Supabase Webhooks will listen for key database events (e.g., `savings_goal_reached`, `daily_budget_approaching_limit`) and trigger Edge Functions.
- **Silent Background Pushes (Data Sync):** Specifically for offline-first architecture, when a change is detected on the DB, the Edge Function fires a **Silent Push** (via APNs for iOS, FCM for Android with generic data payloads, no visual alert). This wakes the OS background worker on the device to perform a stealth SQLite sync, ensuring data is instantly fresh the second the user actually opens the app.
- **Visible Push Notifications:** For gamification logic (Quest completions, budget warnings), the Edge Functions will format visual notifications and fire them through standard FCM/APNs. By managing push tokens via the Edge environment, the business logic remains entirely decoupled from the mobile clients.

---

## Phase 1: Backend Foundation Execution Plan

The goal of Phase 1 is to initialize the Supabase architecture, translate our Local SQLite KMP schema into a production-grade PostgreSQL database, and heavily secure it using Row-Level Security (RLS). 

### Step 1: Project Initialization
- Create a `/backend` directory in the root of the BudgetQuest repository.
- Initialize the Supabase CLI (`supabase init`) to handle local development via Docker.
- Link the CLI to a remote Supabase Cloud project (to be created on the Free Tier).

### Step 2: Database Schema Definitions
We will write the primary migration file (`0001_initial_schema.sql`) to explicitly translate the KMP SQLite entities to Postgres:
- **`users` table:** UUID PK, links securely to `auth.users` (Supabase implicit auth layer).
- **`transactions` table:** UUID PK, `amount`, `category`, `createdAt`, `isExpense`, and a `user_id` Foreign Key.
- **`budgets` table:** Limits, tracking resets, category constraints.
- **`quests` & `savings_goals` tables:** Tracking gamified progression variables.

### Step 3: Row-Level Security (RLS) Implementation
PostgREST makes the database publicly accessible. Security must be enforced natively in the database.
- Disable generic public access across all user-data tables.
- Write strict `CREATE POLICY` statements guaranteeing that operations (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) require the executing user's `auth.uid()` to identically match the row's `user_id`.

### Step 4: First Edge Function Setup (Bun/Deno)
- Scaffold out the Supabase Edge Functions directory.
- Prepare a basic `"hello-world"` ping function to validate that Edge deployments are functioning correctly—paving the way for the Live Push Notifications architecture planned in Phase 2.

### Step 5: TypeScript/Data Generation
- Automatically generate the TypeScript/Kotlin types from the defined Postgres schema representing the strict Data Transfer Objects (DTOs) the Frontend Phase 8 Ktor Client will consume.

---

## Phase 2: Authentication & Security Architecture (Login Module)

### Prerequisites Before Implementation
1. **Initialize Remote Supabase Project:** Ensure a cloud instance of Supabase is active to support GoTrue backend operations.
2. **Setup Mail/SMS Gateways:** Secure API keys for external SMTP providers (e.g., Resend, Sendgrid) and SMS Sandboxes (e.g., Twilio) in the Supabase config to facilitate OTP token delivery.

The goal of Phase 2 is to deploy a completely secure, scalable multi-factor authentication (MFA) footprint using Supabase's native GoTrue engine, supporting Email, Mobile, OTPs, and cleanly enabling client-side biometrics.

### 1. Identity & Access Management (IAM) Strategy
Instead of building custom password-hashing schemes, we will offload all core authentication to **Supabase Auth**. This provides enterprise-grade security and avoids exposing raw credentials to our custom edge functions.

- **Email & Password:** Native Supabase Auth handling.
- **Mobile Number Authentication:** Configured via Supabase Auth SMS Providers (e.g., Twilio or MessageBird integration).
- **Password Recovery Flow:** The backend will utilize the predefined GoTrue `/recover` endpoints. Supabase natively emails a secure, ephemeral 6-digit OTP or Magic Link to the user. Upon validation, the user receives an AAL1 JWT specifically permitted by GoTrue to hit the `/user` update password endpoint, securely updating the credential without custom backend reset logic.

### 2. Two-Factor Authentication (2FA / OTP)
- **Supabase MFA (Multi-Factor Auth):** We will enable the Supabase MFA APIs. 
- **Workflow Phase 1 (Enrollment):** When a user successfully logs in via password, the backend issues an Assurances Level 1 (AAL1) JWT. The database checks if the user requires 2FA.
- **Workflow Phase 2 (Challenge):** If 2FA is required, the Supabase API natively dispatches an OTP (via SMS or Email, or TOTP Authenticator). The client submits the OTP payload back to Supabase.
- **Workflow Phase 3 (Verification):** Supabase validates the OTP and automatically upgrades the JWT session token to Assurances Level 2 (AAL2), granting full database access via RLS.

### 3. Biometric Interaction (Backend Perspective)
- **Stateless Trust:** Biometric authentication (FaceID/Fingerprint) is entirely a local client-side hardware concern. The backend does not store biometric signatures.
- **Token Persistence:** The backend simply generates long-lived Refresh Tokens. The client encrypts these tokens locally. When a user authenticates biometrically on the device, the client unlocks the Refresh Token, sends it to the backend, and instantly receives a fresh Access Token without hitting the OTP/Password friction layer.

### 4. Implementation Steps
- [ ] Configure Supabase Auth settings in `config.toml` (enabling Phone sign-ins).
- [ ] Write integration scripts to connect a Sandbox SMS Gateway for local mobile OTP testing.
- [ ] Update RLS policies to rigorously check `auth.jwt() -> 'aal'` claims (mandating AAL2 access levels for sensitive transaction queries).
