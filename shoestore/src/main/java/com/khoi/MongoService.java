package com.khoi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoService {

    private static final Dotenv dotenv = Dotenv.configure().load();

    private static final String CONNECTION_URI = dotenv.get("MONGO_URI");
    private static final String DATABASE_NAME = dotenv.get("MONGO_DATABASE");
    private static final String COLLECTION_NAME = dotenv.get("MONGO_COLLECTION");

    public List<StoreAppFX.Product> getAllProducts() {
        List<StoreAppFX.Product> productList = new ArrayList<>();

        if (CONNECTION_URI == null || CONNECTION_URI.isEmpty()) {
            throw new RuntimeException("\n[LỖI]: Không thể đọc được biến MONGO_URI từ file .env!\n" +
                    "Vui lòng kiểm tra lại tên file và vị trí đặt file .env.");
        }

        try (MongoClient mongoClient = MongoClients.create(CONNECTION_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME != null ? DATABASE_NAME : "AdidasStoreDB");
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME != null ? COLLECTION_NAME : "products");

            for (Document doc : collection.find()) {
                StoreAppFX.Product product = new StoreAppFX.Product(
                        doc.getString("name"),
                        doc.getString("brand"),
                        doc.getString("fullNote"),
                        doc.getString("cardNote"),
                        doc.getString("price"),
                        doc.getString("imagePath")
                );
                productList.add(product);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi truy vấn dữ liệu từ MongoDB: " + e.getMessage());
            throw e;
        }
        return productList;
    }

   public void seedDataIfNeeded() {
        if (CONNECTION_URI == null || CONNECTION_URI.isEmpty()) return;

        try (MongoClient mongoClient = MongoClients.create(CONNECTION_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME != null ? DATABASE_NAME : "AdidasStoreDB");
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME != null ? COLLECTION_NAME : "products");

            if (collection.countDocuments() == 0) {
                List<Document> sampleDocs = new ArrayList<>();
                String exclusionFull = "This product is excluded from all\npromotional discounts and offers.";
                String exclusionShort = "This product is excluded fr...";

                sampleDocs.add(new Document("name", "4DFWD PULSE SHOES")
                        .append("brand", "Adidas")
                        .append("fullNote", exclusionFull)
                        .append("cardNote", exclusionShort)
                        .append("price", "$160.00")
                        .append("imagePath", "img1.png"));

                sampleDocs.add(new Document("name", "FORUM MID SHOES")
                        .append("brand", "Adidas")
                        .append("fullNote", exclusionFull)
                        .append("cardNote", exclusionShort)
                        .append("price", "$100.00")
                        .append("imagePath", "img2.png"));

                sampleDocs.add(new Document("name", "SUPERNOVA SHOES")
                        .append("brand", "Adidas")
                        .append("fullNote", "NMD City Stock 2")
                        .append("cardNote", "NMD City Stock 2")
                        .append("price", "$150.00")
                        .append("imagePath", "img3.png"));

                sampleDocs.add(new Document("name", "NMD City Stock 2")
                        .append("brand", "Adidas")
                        .append("fullNote", "NMD City Stock 2")
                        .append("cardNote", "NMD City Stock 2")
                        .append("price", "$160.00")
                        .append("imagePath", "img4.png"));

                sampleDocs.add(new Document("name", "4DFWD PULSE SHOES")
                        .append("brand", "Adidas")
                        .append("fullNote", "NMD City Stock 2")
                        .append("cardNote", "NMD City Stock 2")
                        .append("price", "$160.00")
                        .append("imagePath", "img5.png"));

                // 6. FORUM MID SHOES (Mẫu số 6)
                sampleDocs.add(new Document("name", "FORUM MID SHOES")
                        .append("brand", "Adidas")
                        .append("fullNote", exclusionFull)
                        .append("cardNote", exclusionShort)
                        .append("price", "$120.00")
                        .append("imagePath", "img6.png"));

                // 7. 4DFWD PULSE SHOES (Mẫu số 7)
                sampleDocs.add(new Document("name", "4DFWD PULSE SHOES")
                        .append("brand", "Adidas")
                        .append("fullNote", exclusionFull)
                        .append("cardNote", exclusionShort)
                        .append("price", "$160.00")
                        .append("imagePath", "img1.png"));

                sampleDocs.add(new Document("name", "FORUM MID SHOES")
                        .append("brand", "Adidas")
                        .append("fullNote", exclusionFull)
                        .append("cardNote", exclusionShort)
                        .append("price", "$100.00")
                        .append("imagePath", "img2.png"));

                collection.insertMany(sampleDocs);
                System.out.println("Đã khởi tạo thành công ĐẦY ĐỦ dữ liệu mẫu lên MongoDB!");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo dữ liệu khởi tạo: " + e.getMessage());
        }
    }
}