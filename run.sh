./mvnw clean install
docker build -t jumia/example-app .
cd docker
docker-compose up -d
cd ../..
git clone --progress -v "https://github.com/aboelsoud3/Jumia-Angular.git"
cd Jumia-Angular
npm install
ng serve --open

