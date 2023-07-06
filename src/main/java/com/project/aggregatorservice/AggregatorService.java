public class AggregatorService {
    @Autowired
    AggregatorRepo aggregatorRepo;

    @Autowired
    Service aggregatorService;

    public List<AggregatorService> saveAggregatorService(AggregatorService aggregator) {
        List<AggregatorService> aggregatorObj = new ArrayList<AggregatorService>();
        aggregator.getItems().forEach(d -> {
            d.getItem().forEach(i -> {
                AggregatorService AggregatorServiceObj = new AggregatorService();
                AggregatorServiceObj.setDeliveryDate(aggregator.getDeliveryDate());
                AggregatorServiceObj.setItemName(d.getItemName());
                AggregatorServiceObj.setQuantity(i.getQuantity());
                AggregatorServiceObj.setItemColor(i.getItemColor());
                aggregatorObj.add(AggregatorServiceObj);
            });
        });
        aggregatorRepo.saveAll(aggregatorObj);
        aggregatorService.createInvoiceAggregatorService(aggregator);
        return aggregatorObj;
    }

    public List<AggregatorService> getAggregatorService(String itemName, String itemColor, String deliveryDate)
            throws UnsupportedEncodingException {
        List<AggregatorService> AggregatorServices = new ArrayList<>();
        aggregatorRepo
                .findAll(Specification.where(itemNameEquals(itemName)).and(itemColorEquals(itemColor))
                        .and(deliveryDateEquals(deliveryDate)))
                .forEach(updated -> AggregatorServices.add((AggregatorService) updated));
        return AggregatorServices;
    }

    private Specification<AggregatorService> itemNameEquals(final String itemName) {

        return StringUtils.isEmpty(itemName) ? null
                : (root, query, builder) -> builder.equal(root.get("itemName"), itemName);
    }

    public ResponseEntity<Object> updateInvoice(int id, AggregatorService aggregatorObj) {
        Optional<AggregatorService> AggregatorService1 = aggregatorRepo.findById(id);
        AggregatorService AggregatorServiceObj = AggregatorService1.get();
        if (AggregatorServiceObj != null) {
            AggregatorServiceObj.setItemName(aggregatorObj.getItemName());
            AggregatorServiceObj.setItemColor(aggregatorObj.getItemColor());
            AggregatorServiceObj.setQuantity(aggregatorObj.getQuantity());
            AggregatorServiceObj.setId(aggregatorObj.getId());
            return new ResponseEntity<>(aggregatorRepo.save(AggregatorServiceObj), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> deleteAggregatorService(int id) {
        try {
            // check if employee exist in database
            Optional<AggregatorService> AggregatorService1 = aggregatorRepo.findById(id);
            AggregatorService aggregatorObj = AggregatorService1.get();

            if (AggregatorService1 != null) {
                aggregatorRepo.deleteById(aggregatorObj.getId());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<AggregatorService> getDeliveryBody(String deliveryDate) {

        List<AggregatorService> AggregatorServices = new ArrayList<>();
        aggregatorRepo.findAll(Specification.where(deliveryDateEquals(deliveryDate)))
                .forEach(updated -> AggregatorServices.add((AggregatorService) updated));
        return AggregatorServices;
    }

    private Specification<AggregatorService> itemColorEquals(final String itemColor) {

        return StringUtils.isEmpty(itemColor) ? null
                : (root, query, builder) -> builder.equal(root.get("itemColor"), itemColor);
    }

    private Specification<AggregatorService> deliveryDateEquals(final String deliveryDate) {
        if (!StringUtils.isEmpty(deliveryDate)) {
            LocalDate localDate = LocalDate.parse(deliveryDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return StringUtils.isEmpty(deliveryDate) ? null
                    : (root, query, builder) -> builder.equal(root.get("deliveryDate"), localDate);

        }
        return null;
    }
}
