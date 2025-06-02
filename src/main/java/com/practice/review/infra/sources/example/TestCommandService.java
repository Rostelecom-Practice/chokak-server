package com.practice.review.infra.sources.example;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewReactions;
import com.practice.review.infra.sources.JpaReviewCommandService;
import com.practice.review.infra.sources.RestReviewCommandService;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;

import static com.practice.review.infra.sources.example.TestCityRestaurantsReviewSource.BASE_URL;

@Component
public class TestCommandService extends
        RestReviewCommandService<
                        CreateReviewRequest,
                        AddReactionRequest,
                        CreateReviewRequest
                        > {

    private final static Function<ReviewDetails, CreateReviewRequest> submitMapper = details -> {
        CreateReviewRequest req = new CreateReviewRequest();
        req.authorId = details.getAuthorId();
        req.organizationId = details.getOrganizationId();
        req.title = details.getTitle();
        req.content = details.getContent();
        req.ratingValue = details.getRating().getValue();
        req.parentReviewId = details.getParentReviewId().orElse(null);
        return req;
    };

    private final static Function<RestReviewCommandService.ReactCommandParams, AddReactionRequest> reactMapper = params ->
            new AddReactionRequest(params.reactionType);

    private final static Function<RestReviewCommandService.ReplyCommandParams, CreateReviewRequest> replyMapper = params -> {
        ReviewDetails reply = params.reply;
        CreateReviewRequest req = new CreateReviewRequest();
        req.authorId = reply.getAuthorId();
        req.organizationId = reply.getOrganizationId();
        req.title = reply.getTitle();
        req.content = reply.getContent();
        req.ratingValue = reply.getRating().getValue();
        req.parentReviewId = params.parentReviewId;
        return req;
    };


    public TestCommandService(JpaReviewCommandService commandService) {
        super(BASE_URL,
                submitMapper,
                reactMapper,
                replyMapper,
                commandService);
    }


}

