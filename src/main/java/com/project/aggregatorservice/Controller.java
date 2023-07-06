public class Controller {

    @Autowired
    AggregatorService AggregatorService;

    @PostMapping("/aggregator")
    private List<AggregatorService> saveDelivery(@RequestBody AggregatorService AggregatorService) {
        return AggregatorService.saveAggregatorService(AggregatorService);
    }

    @GetMapping("/aggregator")
    private List<AggregatorService> getAggregatorService(@RequestParam(required = false) String itemName,
            @RequestParam(required = false) String itemColor, @RequestParam(required = false) String deliveryDate)
            throws UnsupportedEncodingException {
        return AggregatorService.getAggregatorService(itemName, itemColor, deliveryDate);
    }

    @PutMapping("/aggregator/{id}")
    private ResponseEntity<Object> updateInvoice(@PathVariable("id") int id,
            @RequestBody AggregatorService aggregator) {
        return AggregatorService.updateInvoice(id, aggregator);
    }

    @DeleteMapping("/aggregator/{id}")
    private ResponseEntity<Object> deleteRequest(@PathVariable("id") int id) {
        return AggregatorService.deleteAggregatorService(id);
    }

}
