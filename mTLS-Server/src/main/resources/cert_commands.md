
- To generate private key for CA
  openssl genrsa -out ca.key 2048

- To generate public cert of CA
  openssl req -x509 -new -nodes \
  -key ca.key \
  -subj "/CN=MyCA" \
  -days 3650 \
  -out ca.crt


- To generate the private key for server - 1
  openssl genrsa -out server.key 2048


- To generate the .csr
  openssl req -new \
  -key server.key \
  -subj "/CN=localhost" \
  -out server.csr

- To generate a final cert using the .csr file

openssl x509 -req \
-in server.csr \
-CA ca.crt \
-CAkey ca.key \
-CAcreateserial \
-out server.crt \
-days 3650

-- For second server

openssl genrsa -out client.key 2048

openssl req -new \
-key client.key \
-subj "/CN=myclient" \
-out client.csr

openssl x509 -req \
-in client.csr \
-CA ca.crt \
-CAkey ca.key \
-CAcreateserial \
-out client.crt \
-days 3650

-- For generating pkcs12 (keystore format) files for both the servers

openssl pkcs12 -export \
-in server.crt \
-inkey server.key \
-out server.p12 \
-name server \
-CAfile ca.crt \
-caname root \
-passout pass:password


openssl pkcs12 -export \
-in client.crt \
-inkey client.key \
-out client.p12 \
-name client \
-CAfile ca.crt \
-caname root \
-passout pass:password


-- For generating pkcs12 (keystore format) file for ca.crt that will be common in both servers
keytool -importcert \
-alias ca \
-file ca.crt \
-keystore ca.p12 \
-storetype PKCS12 \
-storepass password \
-noprompt

-- Commands to move the files

mv server.p12 ../mTLS-Server/src/main/resources/
cp ca.p12 ../mTLS-Server/src/main/resources/


mv client.p12 ../mTLS-Client/src/main/resources/
cp ca.p12 ../mTLS-Client/src/main/resources/