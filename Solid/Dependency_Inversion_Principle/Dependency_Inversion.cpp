#include <iostream>
using namespace std;

// Interface provided for classes to implement different recommendation strategies
class RecommendationStrategy {
public:
    virtual void getRecommendations() = 0;
    virtual ~RecommendationStrategy() {}
};

// Class implementing recommendations based on recently added
class RecentlyAdded : public RecommendationStrategy {
public:
    void getRecommendations() override {
        cout << "Showing recently added content..." << endl;
    }
};

// Class implementing recommendations based on trending now
class TrendingNow : public RecommendationStrategy {
public:
    void getRecommendations() override {
        cout << "Showing trending content..." << endl;
    }
};

// Class implementing recommendations based on Genre
class GenreBased : public RecommendationStrategy {
public:
    void getRecommendations() override {
        cout << "Showing content based on your favorite genres..." << endl;
    }
};

// Class implementing the Recommendation Engine (High - level module)
class RecommendationEngine {
private:
    RecommendationStrategy* strategy;

public:
    RecommendationEngine(RecommendationStrategy* strategy) : strategy(strategy) {}

    void recommend() {
        strategy->getRecommendations();
    }
};

// Main driver code
int main() {
    RecommendationStrategy* strategy = new TrendingNow(); // could also be RecentlyAdded or GenreBased
    RecommendationEngine engine(strategy);
    engine.recommend();

    delete strategy;
    return 0;
}
