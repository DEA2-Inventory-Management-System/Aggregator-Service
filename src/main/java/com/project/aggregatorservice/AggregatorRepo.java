public interface AggregatorRepo<T extends AggregatorService>
        extends JpaRepository<T, Integer>, JpaSpecificationExecutor<AggregatorService> {
    Optional<Request> findByDeliveryDate(String deliveryDate);
}
