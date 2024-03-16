Remider: To run this project please use your own aws creds


Dropbox-Equivalent Service

Project Description

The goal of this project is to implement a simplified Dropbox-like service where users can upload, retrieve, and
manage their files through a set of RESTful APIs. The service should also support the storage of metadata for each

uploaded file, such as the file name, creation timestamp, and more.

Features

APIs
1. Upload File API: Allow users to upload files onto the platform.
Endpoint: POST /files/upload
Input: File binary data, file name, metadata (if any)
Output: A unique file identifier
Metadata to Save: File name, createdAt timestamp, size, file type
2. Read File API: Retrieve a specific file based on a unique identifier.
Endpoint: GET /files/{fileId}
Input: Unique file identifier
Output: File binary data
3. Update File API: Update an existing file or its metadata.
Endpoint: PUT /files/{fileId}
Input: New file binary data or new metadata
Output: Updated metadata or a success message
4. Delete File API: Delete a specific file based on a unique identifier.
Endpoint: DELETE /files/{fileId}

Input: Unique file identifier
Output: A success or failure message
5. List Files API: List all available files and their metadata.
Endpoint: GET /files
Input: None
Output: A list of file metadata objects, including file IDs, names, createdAt timestamps, etc.

Technologies
Backend: Choose any backend language and framework you are comfortable with (e.g., Java, Flask, Django,
Express.js, FastAPI).
Database: You can use a database such as SQLLite, MySQL, PostgreSQL, or MongoDB to store the files and
metadata.
Storage: You may use local storage (File system of host machine or DB) for simplicity or cloud storage services like
AWS S3 for storing files.

Architecture
1. Backend Server: Responsible for handling API requests.
2. Database: Stores metadata about the files.
3. File Storage: The actual storage where files are saved.
