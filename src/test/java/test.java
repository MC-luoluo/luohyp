public class test {
    public static void main(String[] args) {
        int level = 4;
        System.out.println(switch (level) {
            case 1 -> "初来乍到";
            case 2 -> "未经雕琢";
            case 3 -> "初窥门径";
            case 4 -> "学有所成";
            case 5 -> "驾轻就熟";
            case 6 -> "历练老成";
            case 7 -> "技艺精湛";
            case 8 -> "炉火纯青";
            case 9 -> "技惊四座";
            case 10 -> "巧夺天工";
            case 11 -> "闻名于世";
            case 12 -> "建筑大师";
            default -> "null";
        });
    }
}
