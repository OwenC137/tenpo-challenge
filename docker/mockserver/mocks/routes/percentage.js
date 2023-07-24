module.exports = [
  {
    id: "get-percentage",
    url: "/api/percentage",
    method: "GET",
    variants: [
          {
              id: "success", // id of the variant
              type: "middleware",
              options: {
                  middleware: (req, res, next, core) => { // middleware to use
                      res.status(200);
                      res.send({
                            "value": (Math.random() * 10).toFixed(2)
                      });
                  }
              }
          }
      ]
  },
];
