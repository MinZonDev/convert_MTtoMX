public class Main {
    public static void main(String[] args) {
        // Test MT202 to pacs.009 conversion
        MT202ToPACS009Converter mt202ToPacs009Converter = new MT202ToPACS009Converter();
        String mt202Message =
                ":20:REFERENCE\n" +
                        ":52A:SENDERBANK\n" +
                        ":57A:RECEIVERBANK\n" +
                        ":32B:CURRENCYAMOUNT\n" +
                        ":30:010101\n" +
                        ":20:REFERENCE";

        String pacs009Message = mt202ToPacs009Converter.mt202ToPacs009(mt202Message);
        System.out.println("pacs.009 Message from MT202:");
        System.out.println(pacs009Message);
//        // Test MT900 to CAMT.054 conversion
//        MT900ToCAMT054Converter mt900ToCAMT054Converter = new MT900ToCAMT054Converter();
//        String mt900Message =
//                ":20:REFERENCE\n" +
//                        ":25:ACCOUNT1234567890\n" +
//                        ":28C:12345/1\n" +
//                        ":60F:C240101USD12345,67\n" +
//                        ":61:0101010101C12345,67NCHK1234567890123456\n" +
//                        "DRAFT BANK\n" +
//                        ":86:TRANSACTION DETAILS\n" +
//                        "-";
//
//        String camt054Message = mt900ToCAMT054Converter.mt900ToCamt054(mt900Message);
//        System.out.println("CAMT.054 Message from MT900:");
//        System.out.println(camt054Message);
//
//        // Test MT910 to CAMT.054 conversion
//        MT910ToCAMT054Converter mt910ToCAMT054Converter = new MT910ToCAMT054Converter();
//        String mt910Message =
//                ":20:REFERENCE\n" +
//                        ":25:ACCOUNT1234567890\n" +
//                        ":28C:12345/1\n" +
//                        ":60F:C240101USD12345,67\n" +
//                        ":61:0101010101C12345,67NCHK1234567890123456\n" +
//                        "DRAFT BANK\n" +
//                        ":86:TRANSACTION DETAILS\n" +
//                        "-";
//
//        String camt054MessageFromMT910 = mt910ToCAMT054Converter.mt910ToCamt054(mt910Message);
//        System.out.println("CAMT.054 Message from MT910:");
//        System.out.println(camt054MessageFromMT910);
    }
}
