package anchor.v1.api;

import anchor.v1.ApiException;
import anchor.v1.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnchorApiTest {
    private final AnchorApi api = new AnchorApi();


    /**
     * anchorBlock
     *
     * 블록체인 데이터를 앵커링 하기 위해서 사용할 수 있습니다. &#x60;payload&#x60;의 데이터는 자유롭게 구성될 수 있지만, &#x60;payload.id&#x60; 필드를 설정할 경우 해당 필드를 통해서 조회가 가능합니다. &#x60;payload.id&#x60;를 설정하지 않는 경우에는 기본적으로 &#x60;payload.id &#x3D; SHA256(payload)&#x60;로 설정됩니다.  이때, 해당 operator의 Anchor 트랜잭션은 식별가능한 &#x60;payload.id&#x60;를 가지게 됩니다.  다시 말해서, 동일한 &#x60;payload.id&#x60;가 존재하는 경우에는 anchor가 정상적으로 수행되지 않습니다.  | Code | Description | | - | - | | 0 | 정상적으로 성공 | | 1000 | 알려지지 않은 에러 | | 2101 | Payload ID 중복 에러 |  | 2102 | 설정된 모든 계정의 밸런스 부족 |
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void anchorBlockTest() throws ApiException {
        String authorization = null;
        String xKrn = null;
        V1AnchorRequest body = new V1AnchorRequest();
        V1AnchorResponse response = api.anchorBlock(authorization, xKrn, body);

        // TODO: test validations
    }

    /**
     * Retrieve anchored transaction
     *
     * Retrieve anchored transaction
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getRetrieveanchoredtransactionTest() throws ApiException {
        String operatorId = null;
        String payloadId = null;
        String authorization = null;
        String xKrn = null;
        V1OperatorPayloadResponse response = api.getRetrieveanchoredtransaction(operatorId, payloadId, authorization, xKrn);

        // TODO: test validations
    }

    /**
     * retrieveBlock
     *
     * Retrieve anchored transactions
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveBlockTest() throws ApiException {
        String operatorId = null;
        String authorization = null;
        String xKrn = null;
        Integer size = null;
        Integer fromDate = null;
        Integer toDate = null;
        String cursor = null;
        V1OperatorTxResponse response = api.retrieveBlock(operatorId, authorization, xKrn, size, fromDate, toDate, cursor);

        // TODO: test validations
    }

    /**
     * retrieveOperator
     *
     * Retrieve registered service chain operator
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveOperatorTest() throws ApiException {
        String authorization = null;
        String xKrn = null;
        String operatorId = null;
        V1OperatorResponse1 response = api.retrieveOperator(authorization, xKrn, operatorId);

        // TODO: test validations
    }

    /**
     * Retrieve anchored transaction
     *
     * Retrieve anchored transaction
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveanchoredtransactionTest() throws ApiException {
        String operatorId = null;
        String txHash = null;
        String authorization = null;
        String xKrn = null;
        V1OperatorTxResponse1 response = api.retrieveanchoredtransaction(operatorId, txHash, authorization, xKrn);

        // TODO: test validations
    }

    /**
     * Retrieve registered service chain operators
     *
     * Retrieve registered service chain operators
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveregisteredservicechainoperatorsTest() throws ApiException {
        String authorization = null;
        String xKrn = null;
        Integer size = null;
        Integer fromDate = null;
        Integer toDate = null;
        String cursor = null;
        V1OperatorResponse response = api.retrieveregisteredservicechainoperators(authorization, xKrn, size, fromDate, toDate, cursor);

        // TODO: test validations
    }
}