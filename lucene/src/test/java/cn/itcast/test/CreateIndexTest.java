package cn.itcast.test;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class CreateIndexTest {
    /**
     *将文件导入索引库
     */
    @Test
    public void createIndex1() throws IOException {
        //1.通过FSDirectory.open指定索引目录
        FSDirectory directory = FSDirectory.open(new File("I:\\luneceIndex"));
        //2.声明分词器为标准分词器 StandardAnalyzer
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        //3.声明索引库写出配置IndexWriterConfig
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);//参数一:版本  参数二:分词器
        //4.声明索引库写入对象IndexWriter
        IndexWriter writer = new IndexWriter(directory, config);
        //5.声明File指定读取本地磁盘文件路径
        File fileDir=new File("I:\\searchsource");
        //6.file对象通过listFiles得到文件路径下的所有文件
        File[] files = fileDir.listFiles();
        //7.循环显示并通过writer将doc保存索引库
        for (File file : files) {
            //文件名 file.getName()文件名，
            System.out.println("文件名称"+file.getName());
            //文件内容 FileUtils.readFileToString(file)
            System.out.println("文件内容"+ FileUtils.readFileToString(file));
            //文件大小 FileUtils.sizeOf(file)
            System.out.println("文件大小"+FileUtils.sizeOf(file));
            //文件路径 file.getPath()
            System.out.println("文件路径"+file.getPath());
            /**
             * TextField 文本存储
             * LongField 存储数值
             * Store.YES 需要存储内容到索引库
             */
            //8.声明Document文档对象
            Document document = new Document();
            document.add(new TextField("fileName", file.getName(), Field.Store.YES));
            document.add(new TextField("fileContent", FileUtils.readFileToString(file), Field.Store.YES));
            document.add(new LongField("fileSize", FileUtils.sizeOf(file), Field.Store.YES));
            document.add(new TextField("filePath", file.getPath(), Field.Store.YES));
            //9.writer对象将doc对象写入索引库
            writer.addDocument(document);
        }
        //10.提交数据
        writer.commit();
        //11.writer关闭
        writer.close();
    }

    /**
     * 方法二:批量导入索引库
     * @throws IOException
     */
    @Test
    public void createIndex2() throws IOException {

        //0.创建文档的集合
        Collection<Document> docs=new ArrayList<>();

        //1.通过FSDirectory.open指定索引目录
        FSDirectory directory = FSDirectory.open(new File("I:\\luneceIndex"));
        //2.声明分词器为标准分词器 StandardAnalyzer
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        //3.声明索引库写出配置IndexWriterConfig
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);//参数一:版本  参数二:分词器

        // 设置打开方式：OpenMode.APPEND 会在索引库的基础上追加新索引。
        // OpenMode.CREATE会先清空原来数据，再提交新的索引
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //4.声明索引库写入对象IndexWriter
        IndexWriter writer = new IndexWriter(directory, config);
        //5.声明File指定读取本地磁盘文件路径
        File fileDir=new File("I:\\searchsource");
        //6.file对象通过listFiles得到文件路径下的所有文件
        File[] files = fileDir.listFiles();
        //7.循环显示并通过writer将doc保存索引库
        for (File file : files) {
            /*//文件名 file.getName()文件名，
            System.out.println("文件名称"+file.getName());
            //文件内容 FileUtils.readFileToString(file)
            System.out.println("文件内容"+ FileUtils.readFileToString(file));
            //文件大小 FileUtils.sizeOf(file)
            System.out.println("文件大小"+FileUtils.sizeOf(file));
            //文件路径 file.getPath()
            System.out.println("文件路径"+file.getPath());*/
            /**
             * TextField 文本存储
             * LongField 存储数值
             * Store.YES 需要存储内容到索引库
             */
            //8.创建Document文档对象
            Document document = new Document();
            document.add(new TextField("fileName", file.getName(), Field.Store.YES));
            document.add(new TextField("fileContent", FileUtils.readFileToString(file), Field.Store.YES));
            document.add(new LongField("fileSize", FileUtils.sizeOf(file), Field.Store.YES));
            document.add(new TextField("filePath", file.getPath(), Field.Store.YES));
            docs.add(document);
        }
        //9.writer对象将doc对象写入索引库
        writer.addDocuments(docs);//批量写入

        //10.提交数据
        writer.commit();
        //11.writer关闭
        writer.close();
    }

    /**
     * 方法三:使用IKAnalyzer分词器
     */
    @Test
    public void createIndex3() throws IOException {

        //0.创建文档的集合
        Collection<Document> docs=new ArrayList<>();

        //1.通过FSDirectory.open指定索引目录
        FSDirectory directory = FSDirectory.open(new File("I:\\luneceIndex"));
        //2.声明分词器为标准分词器 StandardAnalyzer
        //StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        IKAnalyzer ikAnalyzer = new IKAnalyzer();
        //3.声明索引库写出配置IndexWriterConfig
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, ikAnalyzer);//参数一:版本  参数二:分词器

        // 设置打开方式：OpenMode.APPEND 会在索引库的基础上追加新索引。
        //             OpenMode.CREATE会先清空原来数据，再提交新的索引
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //4.声明索引库写入对象IndexWriter
        IndexWriter writer = new IndexWriter(directory, config);
        //5.声明File指定读取本地磁盘文件路径
        File fileDir=new File("I:\\searchsource");
        //6.file对象通过listFiles得到文件路径下的所有文件
        File[] files = fileDir.listFiles();
        //7.循环显示并通过writer将doc保存索引库
        for (File file : files) {
            /*//文件名 file.getName()文件名，
            System.out.println("文件名称"+file.getName());
            //文件内容 FileUtils.readFileToString(file)
            System.out.println("文件内容"+ FileUtils.readFileToString(file));
            //文件大小 FileUtils.sizeOf(file)
            System.out.println("文件大小"+FileUtils.sizeOf(file));
            //文件路径 file.getPath()
            System.out.println("文件路径"+file.getPath());*/
            /**
             * TextField 文本存储
             * LongField 存储数值
             * Store.YES 需要存储内容到索引库
             */
            //8.创建Document文档对象
            Document document = new Document();
            document.add(new TextField("fileName", file.getName(), Field.Store.YES));
            document.add(new TextField("fileContent", FileUtils.readFileToString(file), Field.Store.YES));
            document.add(new LongField("fileSize", FileUtils.sizeOf(file), Field.Store.YES));
            document.add(new TextField("filePath", file.getPath(), Field.Store.YES));
            docs.add(document);
        }
        //9.writer对象将doc对象写入索引库
        writer.addDocuments(docs);//批量写入

        //10.提交数据
        writer.commit();
        //11.writer关闭
        writer.close();
    }

    /**
     * 查询索引库
     */
    @Test
    public void queryIndex() throws IOException {
        //1.创建索引库读取对象DirectoryReader.open
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("I:\\indexsources")));
        //2.声明IndexSearcher的搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //3.声明MatchAllDocsQuery查询对象
        Query query = new MatchAllDocsQuery();
        //4.通过indexSearcher对象的search方法进行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        //5.topDocs.scoreDocs方法获取文档集合
        ScoreDoc[] docs = topDocs.scoreDocs;
        //6.循环文档集合，通过文档.doc获取到文档ID
        for (ScoreDoc doc : docs) {
            System.out.println("文档id:"+doc.doc);
            //7.循环内通过indexSearcher.doc(文档.doc)获取到文档对象
            Document document = indexSearcher.doc(doc.doc);
            //文档内容
            System.out.println("名称"+document.getField("fileName"));
            System.out.println("内容"+document.getField("fileContent"));
            System.out.println("大小"+document.getField("fileSize"));
            System.out.println("路径"+document.getField("filePath"));
        }

        //8.topDocs.totalHits可以获取符合条件的文档总数量
        System.out.println("文档总数量:"+topDocs.totalHits);
    }

    /**
     * 通过词条进行查询
     */
    @Test
    public void queryIndex1() throws IOException {
        //1.创建索引库读取对象DirectoryReader.open
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("I:\\luneceIndex")));
        //2.声明IndexSearcher的搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //3.通过词条进行查询
        TermQuery query = new TermQuery(new Term("fileName", "传智播客"));
        //4.通过indexSearcher对象的search方法进行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        //5.topDocs.scoreDocs方法获取文档集合
        ScoreDoc[] docs = topDocs.scoreDocs;
        //6.循环文档集合，通过文档.doc获取到文档ID
        for (ScoreDoc doc : docs) {
            System.out.println("文档id:"+doc.doc);
            //7.循环内通过indexSearcher.doc(文档.doc)获取到文档对象
            Document document = indexSearcher.doc(doc.doc);
            //文档内容
            System.out.println("名称"+document.getField("fileName"));
            System.out.println("内容"+document.getField("fileContent"));
            System.out.println("大小"+document.getField("fileSize"));
            System.out.println("路径"+document.getField("filePath"));
        }

        //8.topDocs.totalHits可以获取符合条件的文档总数量
        System.out.println("文档总数量:"+topDocs.totalHits);
    }

    /**
     * 通过数值范围查询
     */
    @Test
    public void queryIndex2() throws IOException {
        //1.创建索引库读取对象DirectoryReader.open
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("I:\\luneceIndex")));
        //2.声明IndexSearcher的搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //参数1：查询的域，参数2：起始字节数值，参数3：结束字节数值，参数4：包含起始，参数5：包含结束
        Query query = NumericRangeQuery.newLongRange("fileSize",1L,50L,true,true);
        //4.通过indexSearcher对象的search方法进行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        //5.topDocs.scoreDocs方法获取文档集合
        ScoreDoc[] docs = topDocs.scoreDocs;
        //6.循环文档集合，通过文档.doc获取到文档ID
        for (ScoreDoc doc : docs) {
            System.out.println("文档id:"+doc.doc);
            //7.循环内通过indexSearcher.doc(文档.doc)获取到文档对象
            Document document = indexSearcher.doc(doc.doc);
            //文档内容
            System.out.println("名称"+document.getField("fileName"));
            System.out.println("内容"+document.getField("fileContent"));
            System.out.println("大小"+document.getField("fileSize"));
            System.out.println("路径"+document.getField("filePath"));
        }

        //8.topDocs.totalHits可以获取符合条件的文档总数量
        System.out.println("文档总数量:"+topDocs.totalHits);
    }
}
