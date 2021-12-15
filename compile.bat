javac -cp src; src/hcmus/fit/vuongphuc/server/Server.java -d bin
javac -cp src; src/hcmus/fit/vuongphuc/client/Client.java -d bin
if not exist jar mkdir jar
cd bin
jar -cvfm ../jar/server.jar ../META-INF/server-manifest.mf hcmus/fit/vuongphuc/ui/*.class hcmus/fit/vuongphuc/server/*.class hcmus/fit/vuongphuc/constant/*.class
jar -cvfm ../jar/client.jar ../META-INF/client-manifest.mf hcmus/fit/vuongphuc/ui/*.class hcmus/fit/vuongphuc/client/*.class hcmus/fit/vuongphuc/constant/*.class