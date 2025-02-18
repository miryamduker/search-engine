# **Search Engine Web Crawler**

A scalable web crawler that systematically explores all pages of a given website using the BFS algorithm and indexes them into Elasticsearch, enabling fast and efficient search capabilities.

## Demo
👀🚀 Watch the demo video to see the project in action:  [Demo Video](https://youtu.be/xeyb3NQMLMU)

## **Tech Stack**
- **Java & Spring Boot** – Backend development.
- **Kafka** – Manages distributed crawling.
- **Redis** – Prevents duplicate visits to the same page.
- **Elasticsearch** – Indexes crawled pages for search.
- **Docker** – Containerization for easy deployment.

## **Features**
- **Crawling:** Uses BFS to explore all pages of a website.
- **Kafka Integration:** Manages the crawling process efficiently.
- **Redis for Deduplication:** Ensures pages are not visited more than once.
- **Elasticsearch Indexing:** Stores and indexes crawled pages for fast searching.
- **Dockerized Deployment:** Easily deployable using Docker.

## **Try it out**
You can test the API using the Swagger UI:
👉 [Swagger UI](https://miryam-enginesearch.runmydocker-app.com/swagger-ui.html#/)

## **Run Locally**

### **Prerequisites**
You need to have docker installed.

### **Steps**
1. Clone the repository:
   ```sh
   git clone https://github.com/miryamduker/search-engine.git
   ```
2. Go to the projects directory:
   ```sh
   cd search-engine
   ```
3. Start the application using Docker:
   ```sh
   docker-compose -f docker-compose-local.yml up -d
   ```
