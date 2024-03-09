public class Main {
    public static void main(String[] args) {
//        testMT202ToPACS009Conversion();
//        testMT900ToCAMT054Conversion();
//        testMT910ToCAMT054Conversion();
        testMT515ToMXConversion();
    }

    public static void testMT202ToPACS009Conversion() {
        MT202ToPACS009Converter mt202ToPacs009Converter = new MT202ToPACS009Converter();
        String mt202Message =
                "{1:F01BANKUS33AXXX0000000000}{2:I202BANKNGBXXXXN}{4:\n" +
                        ":20:1234567890n" +
                        ":21:REF123456789\n" +
                        ":32A:220106USD123456,78\n" +
                        ":52A:/1234567890\n" +
                        ":54A:/9876543210\n" +
                        ":72:/TIEN CHUYEN KHOAN/\n" +
                        "-}";

        String pacs009Message = mt202ToPacs009Converter.mt202ToPacs009(mt202Message);
        System.out.println("pacs.009 Message from MT202:");
        System.out.println(pacs009Message);
    }

    public static void testMT900ToCAMT054Conversion() {
        MT900ToCAMT054Converter mt900ToCAMT054Converter = new MT900ToCAMT054Converter();
        String mt900Message =
                ":20:REFERENCE\n" +
                        ":25:ACCOUNT1234567890\n" +
                        ":28C:12345/1\n" +
                        ":60F:C240101USD12345,67\n" +
                        ":61:0101010101C12345,67NCHK1234567890123456\n" +
                        "DRAFT BANK\n" +
                        ":86:TRANSACTION DETAILS\n" +
                        "-";

        String camt054Message = mt900ToCAMT054Converter.mt900ToCamt054(mt900Message);
        System.out.println("CAMT.054 Message from MT900:");
        System.out.println(camt054Message);
    }

    public static void testMT910ToCAMT054Conversion() {
        MT910ToCAMT054Converter mt910ToCAMT054Converter = new MT910ToCAMT054Converter();
        String mt910Message =
                ":20:REFERENCE\n" +
                        ":25:ACCOUNT1234567890\n" +
                        ":28C:12345/1\n" +
                        ":60F:C240101USD12345,67\n" +
                        ":61:0101010101C12345,67NCHK1234567890123456\n" +
                        "DRAFT BANK\n" +
                        ":86:TRANSACTION DETAILS\n" +
                        "-";

        String camt054MessageFromMT910 = mt910ToCAMT054Converter.mt910ToCamt054(mt910Message);
        System.out.println("CAMT.054 Message from MT910:");
        System.out.println(camt054MessageFromMT910);
    }

    public static void testMT515ToMXConversion() {
        MT515ToMXConverter mt515ToMXConverter = new MT515ToMXConverter();
        String mt515Message =
                ":16R:CONFPRTY\n" +
                        ":95P::INVE//JANIGB22\n" +
                        ":97A::SAFE//11111111\n" +
                        ":16R:CONFPRTY\n" +
                        ":16R:CONFPRTY\n" +
                        ":95P::SELL//PEFIGB22\n" +
                        ":16R:CONFPRTY";

        String mxMessage = mt515ToMXConverter.mt515ToMx(mt515Message);
        System.out.println("MX Message from MT515:");
        System.out.println(mxMessage);
    }
}
