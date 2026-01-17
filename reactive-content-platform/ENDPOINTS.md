# Reactive Content Platform - API Endpoints

## Admin (Blocking-style)
Base path: `/api/admin`

### Categories
- GET `/api/admin/categories`
- GET `/api/admin/categories/{id}`
- POST `/api/admin/categories`
- PUT `/api/admin/categories/{id}`
- DELETE `/api/admin/categories/{id}`

### Blogs
- GET `/api/admin/blogs`
- GET `/api/admin/blogs/{id}`  (includes full Category details)
- POST `/api/admin/blogs`
- PUT `/api/admin/blogs/{id}`
- DELETE `/api/admin/blogs/{id}`

## Posts (Reactive)
- GET `/api/blogs/{blogId}/posts`  (Flux<Post>)
- POST `/api/blogs/{blogId}/posts`

## Live Feed (Streaming)
- GET `/api/feed/posts`  (SSE stream, `text/event-stream`)
