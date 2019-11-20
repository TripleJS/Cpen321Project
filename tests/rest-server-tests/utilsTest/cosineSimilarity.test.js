const cosineSimilarity = require("../../rest-server/utils/suggestions/cosineSimilarity");


describe("Cosine Simlarity Test", () => {
    test("2 Empty Vectors", () => {
        const v1 = [];
        const v2 = [];

        expect(cosineSimilarity(v1, v2)).toEqual(0);
        
    });

    test("1 Empty vector, 1 non empty vector", () => {
        const v1 = [];
        const v2 = [1, 2, 3, 4, 5];

        expect(cosineSimilarity(v1, v2)).toEqual(0);
    });

    test("2 Vectors Same Length", () => {
        const v1 = [1, 2, 3, 4];
        const v2 = [5, 4, 2, 3];

        expect(cosineSimilarity(v1, v2)).toEqual(0.7702011922499276);
    });

    test("Vector 1 Length > Vector 2 Length", () => {
        const v1 = [1, 2, 3, 4];
        const v2  = [3, 5, 4];

        expect(cosineSimilarity(v1, v2)).toEqual(0.6454972243679028);

    });

    test("Vector 2 Length > Vector 1 Length", () => {
        const v1 = [2, 5, 7];
        const v2 = [1, 4, 9, 8, 12];

        expect(cosineSimilarity(v1, v2)).toEqual(0.550187743144604);
    });
});