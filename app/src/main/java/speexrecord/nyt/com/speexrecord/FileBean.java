package speexrecord.nyt.com.speexrecord;

/**
 * @作者：聂钰谭
 * @创建日期： 2016/5/6 17:27
 * @实现功能：
 * @更改日志：
 */
public class FileBean {
    private String filePath;

    public FileBean(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
